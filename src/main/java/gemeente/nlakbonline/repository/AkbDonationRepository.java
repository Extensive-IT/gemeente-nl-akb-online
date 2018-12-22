package gemeente.nlakbonline.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface AkbDonationRepository extends CrudRepository<AkbDonationDatabaseObject, Integer> {

    @Query("select d.* from akb_donation d where d.account_id = :accountId")
    Stream<AkbDonationDatabaseObject> findByUser(@Param("accountId") String accountId);

    @Query("select d.* from akb_donation d where d.account_id = :accountId and year = :year")
    Stream<AkbDonationDatabaseObject> findByUserAndYear(@Param("accountId") String accountId, @Param("year") int year);

    @Query("select d.* from akb_donation d where d.year = :year")
    Stream<AkbDonationDatabaseObject> findByYear(@Param("year") int year);
}
