package by.symonik.comment_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_comment")
public class Comment {

    @PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private Long id;

    @PrimaryKeyColumn(name = "issue_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private Long issueId;

    @Column("content")
    private String content;

//    @Column("state")
//    private CommentState state;
}
