from fastapi import APIRouter, HTTPException, status
from app.models import (
    CreatorRequestTo, CreatorResponseTo,
    TweetRequestTo, TweetResponseTo, TweetUpdateTo,
    PostRequestTo, PostResponseTo, PostUpdateTo,
    LabelRequestTo, LabelResponseTo, LabelUpdateTo
)
from app.services import CreatorService, TweetService, LabelService, PostService

router = APIRouter(prefix="/api/v1.0")

# ----- Creator Endpoints -----
@router.post("/creators", response_model=CreatorResponseTo, status_code=status.HTTP_201_CREATED)
def create_creator(dto: CreatorRequestTo):
    return CreatorService.create(dto)

@router.get("/creators", response_model=list[CreatorResponseTo], status_code=status.HTTP_200_OK)
def get_all_creators():
    return CreatorService.get_all()

@router.get("/creators/{id}", response_model=CreatorResponseTo, status_code=status.HTTP_200_OK)
def get_creator(id: int):
    return CreatorService.get_by_id(id)

@router.put("/creators", response_model=CreatorResponseTo, status_code=status.HTTP_200_OK)
@router.put("/creators/", response_model=CreatorResponseTo, status_code=status.HTTP_200_OK)
def update_creator(dto: CreatorResponseTo):
    return CreatorService.update(dto)

@router.delete("/creators/{id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_creator(id: int):
    return CreatorService.delete(id)

@router.get("/creators/tweet/{tweet_id}", response_model=CreatorResponseTo, status_code=status.HTTP_200_OK)
def get_creator_by_tweet(tweet_id: int):
    return CreatorService.get_by_tweet_id(tweet_id)

# ----- Tweet Endpoints -----
@router.post("/tweets", response_model=TweetResponseTo, status_code=status.HTTP_201_CREATED)
def create_tweet(dto: TweetRequestTo):
    return TweetService.create(dto)

@router.get("/tweets", response_model=list[TweetResponseTo], status_code=status.HTTP_200_OK)
def get_all_tweets():
    return TweetService.get_all()

@router.get("/tweets/{id}", response_model=TweetResponseTo, status_code=status.HTTP_200_OK)
def get_tweet(id: str):
    try:
        numeric_id = int(id)
    except ValueError:
        raise HTTPException(status_code=404, detail="Tweet not found")
    return TweetService.get_by_id(numeric_id)

@router.put("/tweets", response_model=TweetResponseTo, status_code=status.HTTP_200_OK)
@router.put("/tweets/", response_model=TweetResponseTo, status_code=status.HTTP_200_OK)
def update_tweet(dto: TweetUpdateTo):
    try:
        numeric_id = int(dto.id)
    except (ValueError, TypeError):
        raise HTTPException(status_code=404, detail="Tweet not found")
    update_dto = TweetRequestTo(
        title=dto.title,
        content=dto.content,
        creator_id=dto.creator_id
    )
    return TweetService.update(numeric_id, update_dto)


@router.delete("/tweets/{id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_tweet(id: str):
    try:
        numeric_id = int(id)
        TweetService.delete(numeric_id)
    except ValueError:
        return
    return

@router.get("/tweets/search", response_model=TweetResponseTo, status_code=status.HTTP_200_OK)
def search_tweet(creator_id: int | None = None, title: str | None = None, content: str | None = None):
    search_params = {"creator_id": creator_id, "title": title, "content": content}
    return TweetService.search(**search_params)

# ----- Post Endpoints -----
# PUT endpoints для Post без id в пути – используем PostUpdateTo
@router.put("/posts", response_model=PostResponseTo, status_code=status.HTTP_200_OK)
@router.put("/posts/", response_model=PostResponseTo, status_code=status.HTTP_200_OK)
def update_post_without_id(dto: PostUpdateTo):
    return PostService.update(dto.id, PostRequestTo(content=dto.content, tweet_id=dto.tweet_id))

@router.post("/posts", response_model=PostResponseTo, status_code=status.HTTP_201_CREATED)
def create_post(dto: PostRequestTo):
    return PostService.create(dto)

@router.get("/posts", response_model=list[PostResponseTo], status_code=status.HTTP_200_OK)
def get_all_posts():
    return PostService.get_all()

@router.get("/posts/{id}", response_model=PostResponseTo, status_code=status.HTTP_200_OK)
def get_post(id: str):
    try:
        numeric_id = int(id)
    except ValueError:
        raise HTTPException(status_code=404, detail="Post not found")
    return PostService.get_by_id(numeric_id)

@router.delete("/posts/{id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_post(id: str):
    try:
        numeric_id = int(id)
        PostService.delete(numeric_id)
    except ValueError:
        raise HTTPException(status_code=404, detail="Post not found")
    return

@router.get("/posts/tweet/{tweet_id}", response_model=list[PostResponseTo], status_code=status.HTTP_200_OK)
def get_posts_by_tweet(tweet_id: int):
    return PostService.get_by_tweet_id(tweet_id)

# ----- Label Endpoints -----
@router.put("/labels", response_model=LabelResponseTo, status_code=status.HTTP_200_OK)
@router.put("/labels/", response_model=LabelResponseTo, status_code=status.HTTP_200_OK)
def update_label_without_id(dto: LabelUpdateTo):
    return LabelService.update(dto.id, LabelRequestTo(name=dto.name))

@router.post("/labels", response_model=LabelResponseTo, status_code=status.HTTP_201_CREATED)
def create_label(dto: LabelRequestTo):
    return LabelService.create(dto)

@router.get("/labels", response_model=list[LabelResponseTo], status_code=status.HTTP_200_OK)
def get_all_labels():
    return LabelService.get_all()

@router.get("/labels/{id}", response_model=LabelResponseTo, status_code=status.HTTP_200_OK)
def get_label(id: str):
    try:
        numeric_id = int(id)
    except ValueError:
        raise HTTPException(status_code=404, detail="Label not found")
    return LabelService.get_by_id(numeric_id)

@router.delete("/labels/{id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_label(id: str):
    try:
        numeric_id = int(id)
        LabelService.delete(numeric_id)
    except ValueError:
        raise HTTPException(status_code=404, detail="Label not found")
    return

@router.get("/labels/tweet/{tweet_id}", response_model=list[LabelResponseTo], status_code=status.HTTP_200_OK)
def get_labels_by_tweet(tweet_id: int):
    return LabelService.get_by_tweet_id(tweet_id)
