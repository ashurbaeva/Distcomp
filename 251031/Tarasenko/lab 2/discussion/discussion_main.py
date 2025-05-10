from fastapi import FastAPI
from discussion.discussion_routers import router as discussion_router

app = FastAPI(title="Discussion Service")

app.include_router(discussion_router, prefix="/api/v1.0")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("discussion.discussion_main:app", host="localhost", port=24130, reload=True)
