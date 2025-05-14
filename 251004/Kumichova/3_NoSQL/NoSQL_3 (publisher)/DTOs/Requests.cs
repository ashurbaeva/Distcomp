namespace LAB2.DTOs;

public class UserRequestTo : BaseDto
{
    public string Login { get; set; }
    public string Password { get; set; }
    public string Firstname { get; set; }
    public string Lastname { get; set; }
}

public class TopicRequestTo : BaseDto
{
    public string Title { get; set; }
    public string Content { get; set; }
    public int UserId { get; set; }
    
    public List<string> Tags { get; set; } = new();
    
}

public class TagRequestTo : BaseDto
{
    public string Name { get; set; }
}

public class MessageRequestTo : BaseDto
{
    public string Content { get; set; }
    public int TopicId { get; set; }
}