package ivlevks.simplenettyserver.repository;

import ivlevks.simplenettyserver.entity.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends CrudRepository<Data, Long> {
}
