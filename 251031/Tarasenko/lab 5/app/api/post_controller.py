from fastapi import APIRouter
from uuid import UUID
from app.services.post_service import get_post_by_id

router = APIRouter(prefix="/api/v1.0/posts", tags=["Post"])

@router.get("/{post_id}")
async def read_post(post_id: UUID):
    post = await get_post_by_id(post_id)
    if post:
        return post
    return {"error": "Post not found"}
