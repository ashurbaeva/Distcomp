namespace LAB1.DTOs;

public class UserResponseTo : BaseDto
{
    public string Login { get; set; }
    public string Firstname { get; set; }
    public string Lastname { get; set; }
}

public class TopicResponseTo : BaseDto
{
    public string Title { get; set; }
    public string Content { get; set; }
    public int UserId { get; set; }
}

public class TagResponseTo : BaseDto
{
    public string Name { get; set; }
}

public class MessageResponseTo : BaseDto
{
    public string Content { get; set; }
    public int TopicId { get; set; }
}