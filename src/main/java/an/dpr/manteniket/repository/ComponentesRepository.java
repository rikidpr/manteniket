package an.dpr.manteniket.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import an.dpr.manteniket.domain.Component;

/**
 * 
 * @author rsaez
 *
 */
public interface ComponentesRepository extends CrudRepository<Component, Long> {
    
    String USER_ID="USER_ID";
    String NAME="NAME";
    String TYPE="TYPE";
    
    String SELECT_USER_AND_NAME ="Select c from Component c where c.disabledDate is null and  c.user.id=:"+USER_ID+" AND c.name=:"+NAME;
    String SELECT_USER_AND_TYPE ="Select c from Component c where c.disabledDate is null and  c.user.id=:"+USER_ID+" AND c.type=:"+TYPE;
    String SELECT_USER = "Select c from Component c where c.disabledDate is null and  c.user.id=:"+USER_ID;
    
    String COUNT_USER_AND_TYPE ="Select count(c) from Component c where c.disabledDate is null and  c.user.id=:"+USER_ID+" AND c.type=:"+TYPE;
    String COUNT_USER = "Select count(c) from Component c where c.disabledDate is null and  c.user.id=:"+USER_ID;

    @Query(SELECT_USER_AND_NAME)
    Component findByUserIdAndName(@Param(USER_ID) Long userId, @Param(NAME) String name);
    
    List<Component> findAll();
    
    
    @Query(SELECT_USER)
    List<Component> findByUserId(@Param(USER_ID) Long userId);
    @Query(SELECT_USER)
    List<Component> findByUserId(@Param(USER_ID) Long userId, Sort sort);
    @Query(SELECT_USER)
    Page<Component> findByUserId(@Param(USER_ID) Long userId, Pageable pageable);
    
    @Query(SELECT_USER_AND_TYPE)
    List<Component> findByUserIdAndType(@Param(USER_ID) Long userId, @Param(TYPE) String type);
    @Query(SELECT_USER_AND_TYPE)
    List<Component> findByUserIdAndType(@Param(USER_ID) Long userId, @Param(TYPE) String type, Sort sort);
    @Query(SELECT_USER_AND_TYPE)
    Page<Component> findByUserIdAndType(@Param(USER_ID) Long userId, @Param(TYPE) String type, Pageable pageable);
    
    long count();
    @Query(COUNT_USER)
    long countByUserId(@Param(USER_ID) Long userId);
    @Query(COUNT_USER_AND_TYPE)
    long countByUserIdAndType(@Param(USER_ID) Long userId, @Param(TYPE) String type);
    
}
