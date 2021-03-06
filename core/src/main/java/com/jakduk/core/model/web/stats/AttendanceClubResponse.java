package com.jakduk.core.model.web.stats;

import com.jakduk.core.model.db.AttendanceClub;
import lombok.Data;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 20 오후 10:57
 */

@Data
public class AttendanceClubResponse {
    private List<AttendanceClub> attendances;
    private Integer totalSum;
    private Integer matchSum;
    private Integer average;
}
