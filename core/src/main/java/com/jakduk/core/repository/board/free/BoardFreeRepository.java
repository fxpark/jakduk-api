package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeSimple;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String>, BoardFreeRepositoryCustom {

	Optional<BoardFree> findOneById(String id);
	Optional<BoardFree> findOneBySeq(Integer seq);
	List<BoardFree> findByIdInAndLinkedGalleryIsTrue(List<String> ids);

	@Query(value="{'seq' : ?0}")
	BoardFreeSimple findBoardFreeOfMinimumBySeq(Integer seq);

}
