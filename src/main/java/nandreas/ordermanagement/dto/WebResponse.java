package nandreas.ordermanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebResponse<T>
{
    private T data;

    private String status;

    private String message;

    @JsonProperty("page_info")
    private PageInfoResponse pageInfo;

    @JsonProperty("total_count")
    private Long totalCount;
}
