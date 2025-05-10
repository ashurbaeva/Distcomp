from fastapi import HTTPException
from discussion.discussion_models import Post, PostRequestTo, PostResponseTo
from discussion.discussion_repositories import PostRepository

class PostService:
    @staticmethod
    def create(dto: PostRequestTo) -> PostResponseTo:
        post = Post(id=None, tweet_id=dto.tweet_id, content=dto.content)
        saved_post = PostRepository.create(post)
        return PostResponseTo(id=str(saved_post.id), tweet_id=saved_post.tweet_id, content=saved_post.content)

    @staticmethod
    def get_by_id(post_id: str) -> PostResponseTo:
        post = PostRepository.find_by_id(post_id)
        if not post:
            raise HTTPException(status_code=404, detail="Post not found")
        return PostResponseTo(id=str(post.id), tweet_id=post.tweet_id, content=post.content)

    @staticmethod
    def get_all() -> list[PostResponseTo]:
        return [PostResponseTo(id=str(p.id), tweet_id=p.tweet_id, content=p.content) for p in PostRepository.find_all()]

    @staticmethod
    def update(post_id: str, dto: PostRequestTo) -> PostResponseTo:
        updated_post = PostRepository.update(post_id, Post(tweet_id=dto.tweet_id, content=dto.content))
        return PostResponseTo(id=str(updated_post.id), tweet_id=updated_post.tweet_id, content=updated_post.content)

    @staticmethod
    def delete(post_id: str) -> None:
        if not PostRepository.delete(post_id):
            raise HTTPException(status_code=404, detail="Post not found")
