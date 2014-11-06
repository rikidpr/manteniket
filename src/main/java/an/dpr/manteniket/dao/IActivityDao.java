package an.dpr.manteniket.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Activity;
import an.dpr.manteniket.domain.Bici;

public interface IActivityDao {

    public abstract Activity save(Activity activity);

    public abstract void delete(Long activityId);

    public abstract Activity findById(Long activityId);

    public abstract List<Activity> find(Activity activity, Sort sort, Integer fromPage, Integer numberOfResults);

    public abstract List<Activity> findByBikeAndDates(Bici bike, Date ini, Date fin);

    public abstract long count(Activity activity);

}