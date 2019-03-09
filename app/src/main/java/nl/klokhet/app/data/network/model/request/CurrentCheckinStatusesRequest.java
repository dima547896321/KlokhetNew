package nl.klokhet.app.data.network.model.request;

import java.io.Serializable;

public class CurrentCheckinStatusesRequest implements Serializable {
    private String groupId;
    private String scheduleId;

    public CurrentCheckinStatusesRequest( String scheduleId,String groupId) {
        this.groupId = groupId;
        this.scheduleId = scheduleId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
}
