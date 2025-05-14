package by.symonik.comment_service.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "issue-service", url = "${issue.service.url}")
public interface IssueClient {

    @GetMapping("/api/v1.0/issues/{id}")
    IssueResponseTo readById(@PathVariable("id") @Valid @NotNull Long id);
}
