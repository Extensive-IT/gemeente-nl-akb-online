package gemeente.nlakbonline.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface AkbDonationRepository extends CrudRepository<AkbDonationDatabaseObject, Integer> {

    @Query("select d.* from akb_donation d where d.user = :user")
    Stream<AkbDonationDatabaseObject> findByUser(@Param("user") String user);

    @Query("select d.* from akb_donation d where d.user = :user and year = :year")
    Stream<AkbDonationDatabaseObject> findByUserAndYear(@Param("user") String user, @Param("year") int year);
}
