# discussion/models.py

from enum import Enum
from dataclasses import dataclass, asdict
from datetime import datetime
import json

class PostState(Enum):
    PENDING = "PENDING"
    APPROVE = "APPROVE"
    DELCINE = "DELCINE"

@dataclass
class Post:
    id: int
    tweet: dict  # Можно заменить на полноценную модель, если необходимо
    content: str
    state: PostState = PostState.PENDING
    created_at: str = datetime.utcnow().isoformat()

    def to_json(self):
        def default(o):
            if isinstance(o, Enum):
                return o.value
            if hasattr(o, '__dict__'):
                return o.__dict__
            return str(o)
        return json.dumps(asdict(self), default=default)
