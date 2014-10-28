package an.dpr.manteniket.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import an.dpr.manteniket.domain.Bici;

public interface IBikesDAO {

    public abstract Bici findByIdBici(Long id);

    public abstract Bici findByCodBici(String codBici);

    public abstract List<Bici> findByTipo(String tipo);

    public abstract List<Bici> findByTipo(Bici bici, Sort sort, Integer fromPage, Integer numberOfResults);

    public abstract List<Bici> findAll();

    public abstract List<Bici> findAll(int from, int numberOfResults);

    public abstract List<Bici> findAll(Sort sort);

    public abstract List<Bici> findAll(Sort sort, Integer fromPage, Integer numberOfResults);

    public abstract long count();

    public abstract long count(Bici bici);

    public abstract Bici save(Bici bike);

    public abstract void delete(Long bikeId);

}