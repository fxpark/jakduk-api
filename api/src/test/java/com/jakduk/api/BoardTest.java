package com.jakduk.api;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.BoardDAO;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.jongo.BoardFeelingCount;
import com.jakduk.core.model.jongo.BoardFreeOnBest;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

public class BoardTest extends ApiApplicationTests {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
    private BoardDAO boardDAO;

	@Autowired
	private Jongo jongo;

	/**
	 * java.lang.IllegalArgumentException: Given DBObject must be a BasicDBObject! Object of class [org.jongo.bson.BsonDBObject] must be an instance of class com.mongodb.BasicDBObject
	 *
	 * FIXME 왜 발생하는지 모르겠다. 근데, 얘만 따로 돌려보면 잘 된다.
	 * 스프링 부트 mongodb 설정을 하면 안된다. MongoConfig로 설정 하면 됨.
	 */
	@Test
	public void test01() throws ParseException {
		Optional<BoardFree> boardFree = boardFreeRepository.findOneBySeq(11);
		
		System.out.println("date=" + Locale.ENGLISH.getDisplayName());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.ENGLISH);
		SimpleDateFormat sf = (SimpleDateFormat) df;
		String p1 = sf.toPattern();
		String p2 = sf.toLocalizedPattern();
		
		System.out.println("p1=" + p1);
		System.out.println("p2=" + p2);
		
		/*
		DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		DateTime dt = new DateTime();
		org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String str = fmt.print(dt);
		System.out.println("str=" + str);
		*/
		
		LocalDateTime dateTime1 = LocalDateTime.parse("Thu, 5 Jun 2014 05:10:40 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);
		System.out.println(dateTime1);
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat ff = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.KOREA);
				
		LocalDate ld = LocalDate.now();
		DateTimeFormatter df02 = DateTimeFormatter.ISO_DATE;
		System.out.println("ld=" + ld.format(df02));

		Date date2 = f.parse(ld.format(df02));
		System.out.println("date2=" + date2.getTime());
	}

	@Test
	public void mongoAggregationTest02() {
		
		ArrayList<ObjectId> arrTemp = new ArrayList<ObjectId>();
		arrTemp.add(new ObjectId("54b160d33d96e261974f2cf7"));
		arrTemp.add(new ObjectId("54b2330a3d96026a3de8d3fd"));
		arrTemp.add(new ObjectId("54c256c23d96b24e3f9dd1d5"));
		
		//Map<String, Integer> map = jakdukDAO.getBoardFreeUsersLikingCount(arrTemp);
		Map<String, BoardFeelingCount> map = boardDAO.getBoardFreeUsersFeelingCount(arrTemp);
		
		System.out.println("mongoAggregationTest02=" + map);
	}

	@Test
	public void getBoardFreeCountOfLikeBest01() {
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		List<BoardFreeOnBest> posts = boardDAO.getBoardFreeCountOfLikeBest(new ObjectId(Date.from(instant)));

		System.out.println("getBoardFreeCountOfLikeBest01=" + posts);
	}

	@Test
	public void getGalleriesCount01() {
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(28);
		arrTemp.add(29);
		arrTemp.add(30);
		
		HashMap<String, Integer> galleriesCount = boardDAO.getBoardFreeGalleriesCount(arrTemp);
		System.out.println("getGalleriesCount01=" + galleriesCount);
	}

	@Test
	public void isNumeric() {
		
		String val01 = "10";
		String val02 = "football";
		String val03 = "football1";
		String val04 = "1football";
		String val05 = "1football2";
		
		Pattern pattern = Pattern.compile("[+-]?\\d+");
		
		System.out.println("isNumeric=" + pattern.matcher(val01).matches());
		System.out.println("isNumeric=" + pattern.matcher(val02).matches());
		System.out.println("isNumeric=" + pattern.matcher(val03).matches());
		System.out.println("isNumeric=" + pattern.matcher(val04).matches());
		System.out.println("isNumeric=" + pattern.matcher(val05).matches());
	}

	@Test
	public void getBoardFreeCountOfCommentBest01() {
		
		LocalDate date = LocalDate.now().minusWeeks(1);
		
		System.out.println("date=" + date.format(DateTimeFormatter.BASIC_ISO_DATE));
		
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		
		System.out.println("objectId=" + new ObjectId(Date.from(instant)));
		
		HashMap<String, Integer> boardFreeCount = boardDAO.getBoardFreeCountOfCommentBest(new ObjectId(Date.from(instant)));
		
		System.out.println("boardFreeCount2=" + boardFreeCount);
		
		ArrayList<ObjectId> ids = new ArrayList<ObjectId>();

		Iterator<?> iterator = boardFreeCount.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
			ObjectId objId = new ObjectId(entry.getKey().toString());
			ids.add(objId);
		}
		
		List<BoardFreeOnBest> boardFreeList = boardDAO.getBoardFreeListOfTop(ids);
		
		for (BoardFreeOnBest boardFree : boardFreeList) {
			String id = boardFree.getId().toString();
			Integer count = boardFreeCount.get(id);
			boardFree.setCount(count);
		}

		Comparator<BoardFreeOnBest> byCount = (b1, b2) -> b2.getCount() - b1.getCount();
		Comparator<BoardFreeOnBest> byView = (b1, b2) -> b2.getViews() - b1.getViews();

		boardFreeList = boardFreeList.stream()
				.sorted(byCount.thenComparing(byView))
				.limit(CoreConst.BOARD_TOP_LIMIT)
				.collect(Collectors.toList());
		
		System.out.println("getBoardFreeCountOfCommentBest01=" + boardFreeList);
	}		

	@Test
	public void jongo01() {
		//DB db = mongoClient.getDB("jakduk_test");

		//Jongo jongo = new Jongo(db);
		MongoCollection boardFreeC = jongo.getCollection("boardFree");

		System.out.println(boardFreeC);
		//Map boardFree = boardFreeC.findOne("{seq:1}").as(Map.class);

		Iterator<Map> boardFree = boardFreeC.aggregate("{$project:{_id:1, usersLikingCount:{$size:{'$ifNull':['$usersLiking', []]}}, usersDislikingCount:{$size:{'$ifNull':['$usersDisliking', []]}}}}")
				.and("{$limit:#}", CoreConst.BOARD_TOP_LIMIT)
                .as(Map.class);

		while (boardFree.hasNext()) {
            System.out.println(boardFree.next());
        }
	}

}
