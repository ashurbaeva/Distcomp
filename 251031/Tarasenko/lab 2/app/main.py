from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import JSONResponse
from app.routers import router

app = FastAPI(title="CRUD API for Creator, Tweet, Label, Post")

@app.exception_handler(HTTPException)
async def http_exception_handler(request: Request, exc: HTTPException):
    return JSONResponse(
        status_code=exc.status_code,
        content={
            "errorMessage": exc.detail,
            "errorCode": f"{exc.status_code:03d}01"
        }
    )

@app.on_event("startup")
def seed_data():
    from app.models import CreatorRequestTo
    from app.services import CreatorService
    try:
        CreatorService.get_by_id(1)
    except HTTPException:
        CreatorService.create(
            CreatorRequestTo(
                login="danataraenko07@gmail.com",
                password="somePassword123",
                firstname="Даниил",
                lastname="Тарасенко"
            )
        )

app.include_router(router)
