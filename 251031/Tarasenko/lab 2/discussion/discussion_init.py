from discussion_config import cassandra_session

def create_tables():
    cassandra_session.execute("""
        CREATE TABLE IF NOT EXISTS tbl_post (
            id UUID PRIMARY KEY,
            tweet_id INT,
            content TEXT
        )
    """)

create_tables()
