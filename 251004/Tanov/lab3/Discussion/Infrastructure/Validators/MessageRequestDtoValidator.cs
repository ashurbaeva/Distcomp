using Discussion.DTO.RequestDTO;
using FluentValidation;

namespace Discussion.Infrastructure.Validators;

public class MessageRequestDtoValidator : AbstractValidator<NoteRequestDTO>
{
    public MessageRequestDtoValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}