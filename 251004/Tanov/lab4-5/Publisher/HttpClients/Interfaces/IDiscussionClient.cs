using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<NoteResponseDTO>?> GetMessagesAsync();

    Task<NoteResponseDTO?> GetMessageByIdAsync(long id);

    Task<NoteResponseDTO?> CreateMessageAsync(NoteRequestDTO post);

    Task<NoteResponseDTO?> UpdateMessageAsync(NoteRequestDTO post);

    Task DeleteMessageAsync(long id);
}