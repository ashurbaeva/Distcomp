using System.ComponentModel.DataAnnotations.Schema;

namespace LAB2.Domain;

public abstract class Entity
{
    [Column("id")]
    public int id { get; set; }
}