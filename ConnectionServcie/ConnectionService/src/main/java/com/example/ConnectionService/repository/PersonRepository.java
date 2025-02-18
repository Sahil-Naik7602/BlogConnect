package com.example.ConnectionService.repository;

import com.example.ConnectionService.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<Person,Long> {

    @Query("MATCH (p:Person) WHERE p.userId = $userId RETURN p")
    Optional<Person> findByUserId(Long userId);

    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person)" +
            " WHERE personA.userId = $userId" +
            " RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("WITH $userId AS userId, $name AS name " +
            "CALL apoc.create.node([userId + '_' + name], {userId: userId, name: name}) YIELD node " +
            "RETURN node")
    Person createNode(@Param("userId") Long userId, @Param("name") String name);

}
