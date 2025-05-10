from pydantic import BaseModel, Field
from typing import Union

# Внутренние модели (доменные объекты)
class Creator(BaseModel):
    id: int
    login: str
    password: str
    firstname: str
    lastname: str

class Tweet(BaseModel):
    id: int
    title: str
    content: str
    # У внутренней модели нет alias; хранится как creator_id
    creator_id: int

class Label(BaseModel):
    id: int
    name: str

class Post(BaseModel):
    id: int
    content: str
    # У внутренней модели нет alias; хранится как tweet_id
    tweet_id: int

# DTO для запросов (без id)
class CreatorRequestTo(BaseModel):
    login: str = Field(..., min_length=3)
    password: str = Field(..., min_length=6)
    firstname: str = Field(..., min_length=1)
    lastname: str = Field(..., min_length=1)

class TweetRequestTo(BaseModel):
    title: str
    content: str
    # Принимаем поле "creatorId" из входного JSON и заполняем creator_id
    creator_id: int = Field(..., alias="creatorId")

    class Config:
        allow_population_by_field_name = True

# Для Post: tweetId может быть int или str
class PostRequestTo(BaseModel):
    content: str
    tweet_id: Union[int, str] = Field(..., alias="tweetId")

    class Config:
        allow_population_by_field_name = True

# Для Label: тесты передают "name"
class LabelRequestTo(BaseModel):
    name: str

# DTO для ответов (с id)
class CreatorResponseTo(BaseModel):
    id: int
    login: str = Field(..., min_length=3)
    password: str = Field(..., min_length=6)
    firstname: str = Field(..., min_length=1)
    lastname: str = Field(..., min_length=1)

class TweetResponseTo(BaseModel):
    id: int
    title: str
    content: str
    # Ожидаем, что в ответе будет ключ "creatorId"
    creator_id: int = Field(..., alias="creatorId")

    class Config:
        allow_population_by_field_name = True

# В ответах для Post хотим ключ "tweetId"
class PostResponseTo(BaseModel):
    id: int
    content: str
    tweet_id: int = Field(..., alias="tweetId")

    class Config:
        allow_population_by_field_name = True

class LabelResponseTo(BaseModel):
    id: int
    name: str

    class Config:
        allow_population_by_field_name = True

# DTO для обновления Tweet – id может быть числом или строкой
class TweetUpdateTo(BaseModel):
    id: Union[str, int]
    title: str
    content: str
    creator_id: int = Field(..., alias="creatorId")

    class Config:
        allow_population_by_field_name = True

# DTO для обновления Post – включает id
class PostUpdateTo(BaseModel):
    id: int
    content: str
    tweet_id: Union[int, str] = Field(..., alias="tweetId")

    class Config:
        allow_population_by_field_name = True

# DTO для обновления Label – включает id
class LabelUpdateTo(BaseModel):
    id: int
    name: str

    class Config:
        allow_population_by_field_name = True
