from fastapi import APIRouter
from discussion.discussion_models import PostRequestTo, PostResponseTo
from discussion.discussion_services import PostService

router = APIRouter()

@router.post("/posts", response_model=PostResponseTo)
def create_post(dto: PostRequestTo):
    return PostService.create(dto)

@router.get("/posts/{id}", response_model=PostResponseTo)
def get_post(id: str):
    return PostService.get_by_id(id)

@router.get("/posts", response_model=list[PostResponseTo])
def get_all_posts():
    return PostService.get_all()

@router.put("/posts/{id}", response_model=PostResponseTo)
def update_post(id: str, dto: PostRequestTo):
    return PostService.update(id, dto)

@router.delete("/posts/{id}")
def delete_post(id: str):
    PostService.delete(id)
    return {"message": "Post deleted"}
