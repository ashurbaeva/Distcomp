from pydantic import BaseModel
import uuid

class Post(BaseModel):
    id: uuid.UUID
    tweet_id: int
    content: str

class PostRequestTo(BaseModel):
    tweet_id: int
    content: str

class PostResponseTo(BaseModel):
    id: str
    tweet_id: int
    content: str
