import uuid
from discussion.discussion_config import cassandra_session
from discussion.discussion_models import Post

class PostRepository:
    @staticmethod
    def create(post: Post) -> Post:
        post_id = uuid.uuid4()
        cassandra_session.execute("""
            INSERT INTO tbl_post (id, tweet_id, content)
            VALUES (%s, %s, %s)
        """, (post_id, post.tweet_id, post.content))
        return Post(id=post_id, tweet_id=post.tweet_id, content=post.content)

    @staticmethod
    def find_by_id(post_id: str) -> Post | None:
        result = cassandra_session.execute("""
            SELECT id, tweet_id, content FROM tbl_post WHERE id = %s
        """, (uuid.UUID(post_id),)).one()
        if result:
            return Post(id=result.id, tweet_id=result.tweet_id, content=result.content)
        return None

    @staticmethod
    def find_all() -> list[Post]:
        rows = cassandra_session.execute("SELECT id, tweet_id, content FROM tbl_post")
        return [Post(id=row.id, tweet_id=row.tweet_id, content=row.content) for row in rows]

    @staticmethod
    def update(post_id: str, post: Post) -> Post:
        cassandra_session.execute("""
            UPDATE tbl_post SET content = %s WHERE id = %s
        """, (post.content, uuid.UUID(post_id)))
        return Post(id=post_id, tweet_id=post.tweet_id, content=post.content)

    @staticmethod
    def delete(post_id: str) -> bool:
        cassandra_session.execute("DELETE FROM tbl_post WHERE id = %s", (uuid.UUID(post_id),))
        return True
