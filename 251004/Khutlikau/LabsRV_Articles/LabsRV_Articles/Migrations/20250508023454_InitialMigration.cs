using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace LabsRV_Articles.Migrations
{
    /// <inheritdoc />
    public partial class InitialMigration : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "tbl_author",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    login = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    password = table.Column<string>(type: "character varying(128)", maxLength: 128, nullable: false),
                    firstname = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    lastname = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_author", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_sticker",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    name = table.Column<string>(type: "character varying(32)", maxLength: 32, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_sticker", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "tbl_article",
                columns: table => new
                {
                    id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    author_id = table.Column<int>(type: "integer", nullable: false),
                    title = table.Column<string>(type: "character varying(64)", maxLength: 64, nullable: false),
                    content = table.Column<string>(type: "character varying(2048)", maxLength: 2048, nullable: false),
                    created = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    modified = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_article", x => x.id);
                    table.ForeignKey(
                        name: "FK_tbl_article_tbl_author_author_id",
                        column: x => x.author_id,
                        principalTable: "tbl_author",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "tbl_article_sticker",
                columns: table => new
                {
                    articleId = table.Column<int>(type: "integer", nullable: false),
                    stickerId = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_tbl_article_sticker", x => new { x.articleId, x.stickerId });
                    table.ForeignKey(
                        name: "FK_tbl_article_sticker_tbl_article_articleId",
                        column: x => x.articleId,
                        principalTable: "tbl_article",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_tbl_article_sticker_tbl_sticker_stickerId",
                        column: x => x.stickerId,
                        principalTable: "tbl_sticker",
                        principalColumn: "id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_author_id",
                table: "tbl_article",
                column: "author_id");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_id",
                table: "tbl_article",
                column: "id",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_title",
                table: "tbl_article",
                column: "title",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_sticker_articleId_stickerId",
                table: "tbl_article_sticker",
                columns: new[] { "articleId", "stickerId" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_article_sticker_stickerId",
                table: "tbl_article_sticker",
                column: "stickerId");

            migrationBuilder.CreateIndex(
                name: "IX_tbl_author_id",
                table: "tbl_author",
                column: "id",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_author_login",
                table: "tbl_author",
                column: "login",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_sticker_id",
                table: "tbl_sticker",
                column: "id",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_tbl_sticker_name",
                table: "tbl_sticker",
                column: "name",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "tbl_article_sticker");

            migrationBuilder.DropTable(
                name: "tbl_article");

            migrationBuilder.DropTable(
                name: "tbl_sticker");

            migrationBuilder.DropTable(
                name: "tbl_author");
        }
    }
}
