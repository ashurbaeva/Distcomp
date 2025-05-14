using Publisher.DTO.RequestDTO;
using FluentValidation;

namespace Publisher.Infrastructure.Validators;

public class LabelRequestDTOValidator : AbstractValidator<MarkRequestDTO>
{
    public LabelRequestDTOValidator()
    {
        RuleFor(dto => dto.Name).Length(2, 32);
    }
}