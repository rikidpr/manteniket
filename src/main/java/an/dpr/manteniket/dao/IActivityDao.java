package an.dpr.manteniket.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.bean.ActivitySummaryBean;
import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;
import an.dpr.manteniket.exception.ManteniketException;

public interface IActivityDao {

    Activity save(Activity activity);

    void delete(Long activityId);

    Activity findById(Long activityId);

    List<Activity> find(Activity activity, Sort sort, Integer fromPage, Integer numberOfResults);

    List<Activity> findByBikeAndDates(Bici bike, Date ini, Date fin);

    long count(Activity activity);
    
    ActivitySummaryBean getActivitySummary(ActivitySummaryBean params) throws ManteniketException;

}