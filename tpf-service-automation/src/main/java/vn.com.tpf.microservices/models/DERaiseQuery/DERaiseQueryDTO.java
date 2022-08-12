package vn.com.tpf.microservices.models.DERaiseQuery;

@Data
@Builder
@Document(collection="dataentry_raisequery")
public class DERaiseQueryDTO {
    private String queryId;
    private String queryCode;
    private String queryName;
    private Boolean tatHold;
    private String raiseBy;
    private String raiseTo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String raiseOn;

    private String queryStatus;
    private String applicationNo;

    private String stage;
    private String comment;
    private Integer clientId;

    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String lastModifiedDate;
    private String _class;
}
