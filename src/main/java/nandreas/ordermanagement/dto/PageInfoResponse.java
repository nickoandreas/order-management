package nandreas.ordermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInfoResponse
{
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("total_pages")
    private Integer totalPages;
}
