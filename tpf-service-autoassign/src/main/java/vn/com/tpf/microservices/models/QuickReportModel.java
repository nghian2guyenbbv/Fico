package vn.com.tpf.microservices.models;


import com.netflix.discovery.provider.Serializer;
import lombok.Builder;
import lombok.Data;

@Builder
@Serializer
@Data
public class QuickReportModel {

        private String vendorName;

        private long actualTotalQuanlity;

        private long actualQuanlityInDay;

        private long vendorId;

}
