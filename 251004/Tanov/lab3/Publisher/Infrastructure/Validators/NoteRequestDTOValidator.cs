using Publisher.DTO.RequestDTO;
using FluentValidation;

namespace Publisher.Infrastructure.Validators;

public class NoteRequestDTOValidator : AbstractValidator<LabelRequestDTO>
{
    public NoteRequestDTOValidator()
    {
        RuleFor(dto => dto.Content).Length(2, 2048);
    }
}