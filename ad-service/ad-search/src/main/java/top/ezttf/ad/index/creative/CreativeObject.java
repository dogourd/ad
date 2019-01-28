package top.ezttf.ad.index.creative;

import lombok.*;

/**
 * @author yuwen
 * @date 2019/1/27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreativeObject {

    private Long adId;
    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Integer auditStatus;
    private String adUrl;


    public CreativeObject update(CreativeObject object) {
        if (object == null) {
            return this;
        }
        if (object.getAdId() != null) {
            this.adId = object.getAdId();
        }
        if (object.getName() != null) {
            this.name = object.getName();
        }
        if (object.getType() != null) {
            this.type = object.getType();
        }
        if (object.getMaterialType() != null) {
            this.materialType = object.getMaterialType();
        }
        if (object.getHeight() != null) {
            this.height = object.getHeight();
        }
        if (object.getWidth() != null) {
            this.width = object.getWidth();
        }
        if (object.getAuditStatus() != null) {
            this.auditStatus = object.getAuditStatus();
        }
        if (object.getAdUrl() != null) {
            this.adUrl = object.getAdUrl();
        }
        return this;
    }
}
