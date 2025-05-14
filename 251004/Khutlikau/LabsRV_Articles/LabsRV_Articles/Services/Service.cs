using AutoMapper;
using LabsRV_Articles.Repositories;
using LabsRV_Articles.Models.Domain;

namespace LabsRV_Articles.Services
{
    public class Service<TEntity, TRequest, TResponse>
        where TEntity : class, LabsRV_Articles.Models.Domain.IEntity, new()
    {
        protected readonly IRepository<TEntity> _repository;
        protected readonly IMapper _mapper;

        public Service(IRepository<TEntity> repository, IMapper mapper)
        {
            _repository = repository;
            _mapper = mapper;
        }

        // Валидация входных параметров – можно переопределить в наследниках.
        public virtual void Validate(TRequest request)
        {
            // По умолчанию не делаем проверок.
        }

        public virtual TResponse Create(TRequest request)
        {
            Validate(request);
            var entity = _mapper.Map<TEntity>(request);
            var created = _repository.Add(entity);
            return _mapper.Map<TResponse>(created);
        }

        public virtual IEnumerable<TResponse> GetAll()
        {
            var entities = _repository.GetAll();
            return _mapper.Map<IEnumerable<TResponse>>(entities);
        }

        public virtual TResponse GetById(int id)
        {
            var entity = _repository.GetById(id);
            if (entity == null)
                throw new ArgumentException($"{typeof(TEntity).Name} with id {id} not found");
            return _mapper.Map<TResponse>(entity);
        }

        public virtual TResponse Update(int id, TRequest request)
        {
            Validate(request);
            var entity = _repository.GetById(id);
            if (entity == null)
                throw new ArgumentException($"{typeof(TEntity).Name} with id {id} not found");
            _mapper.Map(request, entity);
            var updated = _repository.Update(entity);
            return _mapper.Map<TResponse>(updated);
        }

        public virtual void Delete(int id)
        {
            var success = _repository.Delete(id);
            if (!success)
                throw new ArgumentException($"{typeof(TEntity).Name} with id {id} not found");
        }
    }
}

