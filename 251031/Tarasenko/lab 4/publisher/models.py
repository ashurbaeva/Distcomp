# publisher/models.py

from enum import Enum
from dataclasses import dataclass, asdict
import json
from datetime import datetime

class PostState(Enum):
    PENDING = "PENDING"
    APPROVE = "APPROVE"
    DELCINE = "DELCINE"

@dataclass
class Creator:
    id: int
    name: str

@dataclass
class Tweet:
    id: int
    creator: Creator
    content: str

@dataclass
class Label:
    id: int
    name: str

@dataclass
class Post:
    id: int
    tweet: Tweet
    content: str
    state: PostState = PostState.PENDING
    created_at: str = datetime.utcnow().isoformat()

    def to_json(self):
        # Преобразование объекта в JSON с учетом вложенных сущностей
        def default(o):
            if isinstance(o, Enum):
                return o.value
            if hasattr(o, '__dict__'):
                return o.__dict__
            return str(o)
        return json.dumps(asdict(self), default=default)
