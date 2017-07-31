package com.tumcca.api.job;

import com.google.common.base.Optional;
import com.tumcca.api.db.PicturesDAO;
import com.tumcca.api.model.PicturesCache;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.SundialJobScheduler;
import com.xeiam.sundial.annotations.CronTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * Title.
 * <p/>
 * Triggered every hour.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-12
 */
@CronTrigger(cron = "0 0 0/1 * * ?")
public class TidyJob extends Job {
    static final Logger LOGGER = LoggerFactory.getLogger(TidyJob.class);

    @Override
    public void doRun() throws JobInterruptException {
        LOGGER.info("Start to tidy....");
        final long startTime = System.currentTimeMillis();
        final Long cacheTimeout = (Long) SundialJobScheduler.getServletContext().getAttribute("cacheTimeout");
        final DBI dbi = (DBI) SundialJobScheduler.getServletContext().getAttribute("dbi");
        final String uploadPath = (String) SundialJobScheduler.getServletContext().getAttribute("uploadPath");
        final Date deadline = new Date(System.currentTimeMillis() - cacheTimeout * 1000);
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            final List<PicturesCache> localCaches = picturesDAO.findLocalCaches(Optional.of(deadline));
            for (PicturesCache picturesCache : localCaches) {
                final File fileCached = Paths.get(uploadPath, picturesCache.getStorageName()).toFile();
                if (fileCached.exists()) {
                    fileCached.delete();
                }
                picturesDAO.deleteLocal(Optional.of(picturesCache.getUri()));
            }
            LOGGER.info("completed the clean, time elapsed {} s", new BigDecimal(String.valueOf(System.currentTimeMillis() - startTime)).divide(new BigDecimal("1000"), 3, RoundingMode.CEILING));
        } catch (Exception e) {
            LOGGER.error("tidy error", e);
        }
    }
}
