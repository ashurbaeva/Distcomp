using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<MessageResponseDTO>?> GetMessagesAsync();

    Task<MessageResponseDTO?> GetMessageByIdAsync(long id);

    Task<MessageResponseDTO?> CreateMessageAsync(LabelRequestDTO post);

    Task<MessageResponseDTO?> UpdateMessageAsync(LabelRequestDTO post);

    Task DeleteMessageAsync(long id);
}