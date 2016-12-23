package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 24.
 * @desc     :
 */

@Getter
public class BoardFreeOnRSS {

	private String id;
	
	private Integer seq;

	private CommonWriter writer;
	
	private String subject;
	
	private String content;
}
