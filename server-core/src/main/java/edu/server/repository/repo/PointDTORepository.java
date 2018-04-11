package edu.server.repository.repo;

import edu.server.repository.PointDTOEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointDTORepository extends CrudRepository<PointDTOEntity,Integer> {
    @Query(value = "SELECT TOP :num * FROM POINTS WHERE AUTOID LIKE CONCAT('%',:searchTerm,'%') ORDER BY ID DESC", nativeQuery = true)
    List<PointDTOEntity> fetchLastNForAutoid(@Param("num") int num, @Param("searchTerm") String searchTerm);

    @Query(value = "SELECT TOP :num * FROM POINTS ORDER BY ID DESC", nativeQuery = true)
    List<PointDTOEntity> fetchLastN(@Param("num") int num);
}
