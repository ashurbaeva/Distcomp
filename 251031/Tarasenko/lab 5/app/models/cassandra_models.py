from cassandra.cqlengine.models import Model
from cassandra.cqlengine import columns

class Post(Model):
    __table_name__ = 'tbl_post'

    id = columns.UUID(primary_key=True)
    tweet_id = columns.UUID()
    content = columns.Text()
    timestamp = columns.DateTime()
