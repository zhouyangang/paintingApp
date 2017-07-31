package com.tumcca.api.service;

import io.dropwizard.lifecycle.Managed;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.model.ESIndex;
import com.tumcca.api.model.ESTypes;
import com.tumcca.api.model.WorksES;
import com.tumcca.api.model.WorksHit;
import com.tumcca.api.model.WorksHitResult;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-17
 */
public class ElasticSearchService implements Managed {
    static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchService.class);

    final Node node;
    
    final ObjectMapper mapper = new ObjectMapper();

    public ElasticSearchService(Node node) {
        this.node = node;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("ES start successfully");
    }

    @Override
    public void stop() throws Exception {
        node.close();
        LOGGER.info("ES stopped successfully");
    }
    
    /**
     * Index the works
     * @param worksAddVO
     * @param worksId
     * @throws Exception
     */
    public boolean indexWorks(WorksES worksES, Long worksId) throws Exception{
        final String worksESJson = mapper.writeValueAsString(worksES);
        
        final IndexResponse indexResponse = node.client().prepareIndex(ESIndex.tumcca.toString()
        		, ESTypes.works.toString(), worksId.toString())
                .setSource(worksESJson)
                .execute()
                .actionGet();
        return indexResponse.isCreated();
    }
    
    /**
     * Merge the works
     * @param worksES
     * @param worksId
     * @return
     * @throws Exception
     */
    public boolean mergeWorks(WorksES worksES, Long worksId) throws Exception{
    	ImmutableMap<String, Object> updateWorksMap = ImmutableMap.of(
    			  "tags", worksES.getTags()
    			, "title", worksES.getTitle()
    			, "description", worksES.getDescription()
    			, "updateDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(worksES.getUpdateDate())
    			, "category", worksES.getCategory());
    	final String worksESJson = mapper.writeValueAsString(updateWorksMap);
        UpdateRequest updateRequest = new UpdateRequest(
        		ESIndex.tumcca.toString(), ESTypes.works.toString(), worksId.toString()).doc(worksESJson);
        final UpdateResponse updateResponse = node.client().update(updateRequest).get();
        return updateResponse.isCreated();
    }
    
    /**
     * Update the status of a works
     * @param status
     * @param worksId
     * @return
     * @throws Exception
     */
    public boolean updateWorksStatus(Integer status, Long worksId) throws Exception{
    	ImmutableMap<String, Object> updateWorksMap = ImmutableMap.of(
  			  "status", status);
    	final String worksESJson = mapper.writeValueAsString(updateWorksMap);
        UpdateRequest updateRequest = new UpdateRequest(
        		ESIndex.tumcca.toString(), ESTypes.works.toString(), worksId.toString()).doc(worksESJson);
        final UpdateResponse updateResponse = node.client().update(updateRequest).get();
        return updateResponse.isCreated();
    }
    
    /**
     * Delete the works
     * @param worksId
     * @throws Exception
     */
    public void deleteWorks(Long worksId) throws Exception{
        node.client().prepareDelete(ESIndex.tumcca.toString(), ESTypes.works.toString(), worksId.toString())
        .execute().actionGet();
    }
    
    /**
     * Search for Home page
     * @return
     * @throws Exception
     */
	public WorksHitResult searchAll(int no, int size, Long homePageUid) throws Exception {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("status", 1))
				.mustNot(QueryBuilders.termQuery("authorId", homePageUid));
		SortBuilder sortBuilder = SortBuilders.fieldSort("updateDate").order(SortOrder.DESC);
		SearchResponse searchResponse = node.client()
				.prepareSearch(ESIndex.tumcca.toString())
				.setTypes(ESTypes.works.toString())
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(no)
				.setQuery(queryBuilder)
				.addSort(sortBuilder)
				.setSize(size).execute().actionGet();
		final SearchHits hits = searchResponse.getHits();
		final long total = hits.getTotalHits();
		
		if (total > 0) {
			final List<WorksHit> worksHits = Arrays.asList(hits.getHits()).stream().map(h -> {
				try {
					return new WorksHit(h.getId(), mapper.readValue(h.getSourceAsString(), WorksES.class));
				} catch (Exception e) {
					return null;
				}
			}).collect(Collectors.toList());
			return new WorksHitResult(total, worksHits);
		}
		
		return new WorksHitResult(total, null);
	}
	
	/**
	 * Search for the fixed part in Home Page
	 * @param homePageUid
	 * @return
	 * @throws Exception
	 */
	public WorksHitResult searchByAuthor(Long homePageUid) throws Exception{
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("status", 1))
				.must(QueryBuilders.termQuery("authorId", homePageUid));
		SortBuilder sortBuilder = SortBuilders.fieldSort("updateDate").order(SortOrder.DESC);
		SearchResponse searchResponse = node.client()
				.prepareSearch(ESIndex.tumcca.toString())
				.setTypes(ESTypes.works.toString())
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(queryBuilder)
				.addSort(sortBuilder).execute().actionGet();
		final SearchHits hits = searchResponse.getHits();
		final long total = hits.getTotalHits();
		
		if (total > 0) {
			final List<WorksHit> worksHits = Arrays.asList(hits.getHits()).stream().map(h -> {
				try {
					return new WorksHit(h.getId(), mapper.readValue(h.getSourceAsString(), WorksES.class));
				} catch (Exception e) {
					return null;
				}
			}).collect(Collectors.toList());
			return new WorksHitResult(total, worksHits);
		}
		
		return new WorksHitResult(total, null);
	}
	
	/**
	 * 根据关键字查询
	 * @param no
	 * @param size
	 * @param keyword
	 * @return
	 */
	public WorksHitResult searchByKeyword(int no, int size, String keyword, Long homePageUid){
		
		//.field("title", 1.75f).field("url", 1.55f).field("description", 1.35f);
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("status", 1))
				.mustNot(QueryBuilders.termQuery("authorId", homePageUid))
				.must(QueryBuilders.multiMatchQuery(keyword, "tags", "title", "description", "author"));
		
		FunctionScoreQueryBuilder builder = QueryBuilders.functionScoreQuery(queryBuilder);
		builder.add(ScoreFunctionBuilders.exponentialDecayFunction("updateDate","7d"));
		
		SearchResponse searchResponse = node.client()
				.prepareSearch(ESIndex.tumcca.toString())
				.setTypes(ESTypes.works.toString())
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(builder).execute().actionGet();
		final SearchHits hits = searchResponse.getHits();
		final long total = hits.getTotalHits();
		
		if (total > 0) {
			final List<WorksHit> worksHits = Arrays.asList(hits.getHits()).stream().map(h -> {
				try {
					return new WorksHit(h.getId(), mapper.readValue(h.getSourceAsString(), WorksES.class));
				} catch (Exception e) {
					return null;
				}
			}).collect(Collectors.toList());
			return new WorksHitResult(total, worksHits);
		}
		
		return new WorksHitResult(total, null);
	}
}
