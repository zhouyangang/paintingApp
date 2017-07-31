package com.tumcca.api.model;

import java.util.List;

/**
 * 
 * @author wanzhi
 * @version 1.0
 * @since 2015-07-06
 */
public class WorksHitResult {

	Long total;
	List<WorksHit> worksHits;

	public WorksHitResult(Long total, List<WorksHit> worksHits) {
		this.total = total;
		this.worksHits = worksHits;
	}

	public Long getTotal() {
		return total;
	}

	public List<WorksHit> getWorksHits() {
		return worksHits;
	}

}
