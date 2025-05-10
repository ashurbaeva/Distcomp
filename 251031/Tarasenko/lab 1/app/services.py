from fastapi import HTTPException, status
from app.models import (
    Creator, Tweet, Label, Post,
    CreatorRequestTo, CreatorResponseTo,
    TweetRequestTo, TweetResponseTo, TweetUpdateTo,
    PostRequestTo, PostResponseTo, PostUpdateTo,
    LabelRequestTo, LabelResponseTo, LabelUpdateTo
)
from app.repositories import InMemoryRepository

# Инициализация репозиториев
creator_repo = InMemoryRepository[Creator]()
tweet_repo = InMemoryRepository[Tweet]()
label_repo = InMemoryRepository[Label]()
post_repo = InMemoryRepository[Post]()

# Функция трансформации для Tweet: заменяет ключ "creator_id" на "creatorId"
def transform_tweet(tweet: Tweet) -> dict:
    data = tweet.dict()  # Возвращает данные с ключами как в модели (creator_id)
    if "creator_id" in data:
        data["creatorId"] = data.pop("creator_id")
    return data

# Функция трансформации для Post: заменяет ключ "tweet_id" на "tweetId"
def transform_post(post: Post) -> dict:
    data = post.dict()
    if "tweet_id" in data:
        data["tweetId"] = data.pop("tweet_id")
    return data

# Сервис для Creator
class CreatorService:
    @staticmethod
    def create(dto: CreatorRequestTo) -> CreatorResponseTo:
        creator = Creator(
            id=0,
            login=dto.login,
            password=dto.password,
            firstname=dto.firstname,
            lastname=dto.lastname
        )
        creator = creator_repo.create(creator)
        return CreatorResponseTo(**creator.dict())

    @staticmethod
    def get_by_id(id: int) -> CreatorResponseTo:
        creator = creator_repo.find_by_id(id)
        if not creator:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Creator not found")
        return CreatorResponseTo(**creator.dict())

    @staticmethod
    def get_all() -> list[CreatorResponseTo]:
        return [CreatorResponseTo(**c.dict()) for c in creator_repo.find_all()]

    @staticmethod
    def update(dto: CreatorResponseTo) -> CreatorResponseTo:
        if not creator_repo.find_by_id(dto.id):
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Creator not found")
        creator = Creator(
            id=dto.id,
            login=dto.login,
            password=dto.password,
            firstname=dto.firstname,
            lastname=dto.lastname
        )
        updated = creator_repo.update(dto.id, creator)
        return CreatorResponseTo(**updated.dict())

    @staticmethod
    def delete(id: int) -> None:
        if not creator_repo.delete(id):
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Creator not found")

    @staticmethod
    def get_by_tweet_id(tweet_id: int) -> CreatorResponseTo:
        tweet = tweet_repo.find_by_id(tweet_id)
        if not tweet:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tweet not found")
        creator = creator_repo.find_by_id(tweet.creator_id)
        if not creator:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Creator not found")
        return CreatorResponseTo(**creator.dict())

# Сервис для Tweet
class TweetService:
    @staticmethod
    def create(dto: TweetRequestTo) -> TweetResponseTo:
        tweet = Tweet(
            id=0,
            title=dto.title,
            content=dto.content,
            creator_id=dto.creator_id
        )
        tweet = tweet_repo.create(tweet)
        return TweetResponseTo(**transform_tweet(tweet))

    @staticmethod
    def get_by_id(id: int) -> TweetResponseTo:
        tweet = tweet_repo.find_by_id(id)
        if not tweet:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tweet not found")
        return TweetResponseTo(**transform_tweet(tweet))

    @staticmethod
    def get_all() -> list[TweetResponseTo]:
        return [TweetResponseTo(**transform_tweet(t)) for t in tweet_repo.find_all()]

    @staticmethod
    def update(id: int, dto: TweetRequestTo) -> TweetResponseTo:
        tweet = Tweet(
            id=id,
            title=dto.title,
            content=dto.content,
            creator_id=dto.creator_id
        )
        updated = tweet_repo.update(id, tweet)
        if not updated:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tweet not found")
        return TweetResponseTo(**transform_tweet(updated))

    @staticmethod
    def delete(id: int) -> None:
        if not tweet_repo.delete(id):
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tweet not found")

    @staticmethod
    def search(**kwargs) -> TweetResponseTo:
        for tweet in tweet_repo.find_all():
            match = True
            for key, value in kwargs.items():
                if value is not None and getattr(tweet, key, None) != value:
                    match = False
                    break
            if match:
                return TweetResponseTo(**transform_tweet(tweet))
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Tweet not found")

# Сервис для Label
class LabelService:
    @staticmethod
    def create(dto: LabelRequestTo) -> LabelResponseTo:
        label = Label(
            id=0,
            name=dto.name
        )
        label = label_repo.create(label)
        return LabelResponseTo(**label.dict(by_alias=True))

    @staticmethod
    def get_by_id(id: int) -> LabelResponseTo:
        label = label_repo.find_by_id(id)
        if not label:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Label not found")
        return LabelResponseTo(**label.dict(by_alias=True))

    @staticmethod
    def get_all() -> list[LabelResponseTo]:
        return [LabelResponseTo(**l.dict(by_alias=True)) for l in label_repo.find_all()]

    @staticmethod
    def update(id: int, dto: LabelRequestTo) -> LabelResponseTo:
        label = Label(
            id=id,
            name=dto.name
        )
        updated = label_repo.update(id, label)
        if not updated:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Label not found")
        return LabelResponseTo(**updated.dict(by_alias=True))

    @staticmethod
    def delete(id: int) -> None:
        if not label_repo.delete(id):
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Label not found")

    @staticmethod
    def get_by_tweet_id(tweet_id: int) -> list[LabelResponseTo]:
        labels = [l for l in label_repo.find_all() if getattr(l, 'tweet_id', None) == tweet_id]
        return [LabelResponseTo(**l.dict(by_alias=True)) for l in labels]

# Сервис для Post
class PostService:
    @staticmethod
    def create(dto: PostRequestTo) -> PostResponseTo:
        try:
            numeric_tweet_id = int(dto.tweet_id)
        except ValueError:
            numeric_tweet_id = 0
        post = Post(
            id=0,
            content=dto.content,
            tweet_id=numeric_tweet_id
        )
        post = post_repo.create(post)
        return PostResponseTo(**transform_post(post))
    
    @staticmethod
    def get_by_id(id: int) -> PostResponseTo:
        post = post_repo.find_by_id(id)
        if not post:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
        return PostResponseTo(**transform_post(post))
    
    @staticmethod
    def get_all() -> list[PostResponseTo]:
        return [PostResponseTo(**transform_post(p)) for p in post_repo.find_all()]
    
    @staticmethod
    def update(id: int, dto: PostRequestTo) -> PostResponseTo:
        try:
            numeric_tweet_id = int(dto.tweet_id)
        except ValueError:
            numeric_tweet_id = 0
        post = Post(
            id=id,
            content=dto.content,
            tweet_id=numeric_tweet_id
        )
        updated = post_repo.update(id, post)
        if not updated:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
        return PostResponseTo(**transform_post(updated))
    
    @staticmethod
    def delete(id: int) -> None:
        if not post_repo.delete(id):
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
    
    @staticmethod
    def get_by_tweet_id(tweet_id: int) -> list[PostResponseTo]:
        posts = [p for p in post_repo.find_all() if p.tweet_id == tweet_id]
        return [PostResponseTo(**transform_post(p)) for p in posts]

# Функция трансформации для Post: заменяет ключ "tweet_id" на "tweetId"
def transform_post(post: Post) -> dict:
    data = post.dict()
    if "tweet_id" in data:
        data["tweetId"] = data.pop("tweet_id")
    return data
