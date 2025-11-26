import { useState } from 'react';
import api from '../../services/api';
import { UploadCloud, CheckCircle, AlertCircle, ArrowLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const SubmissaoApo = () => {
  const navigate = useNavigate();
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [status, setStatus] = useState({ type: '', message: '' });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setStatus({ type: '', message: '' });

    // Dados para enviar ao endpoint POST /api/v1/apos
    const payload = {
      alunoId: 1,        // TODO: Pegar dinamicamente do token no futuro
      orientadorId: 1,   // TODO: Selecionar de uma lista
      titulo: titulo,
      descricao: descricao
    };

    try {
      await api.post('/apos', payload);
      setStatus({ type: 'success', message: 'APO submetida com sucesso! Aguarde a avaliação.' });
      setTitulo('');
      setDescricao('');
    } catch (error) {
      console.error(error);
      setStatus({ type: 'error', message: 'Erro ao submeter APO. Verifique a conexão.' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-2xl mx-auto bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
        
        <button onClick={() => navigate('/aluno/dashboard')} className="text-gray-500 hover:text-blue-600 flex items-center gap-2 mb-6 transition-colors">
            <ArrowLeft size={20} /> Voltar ao Dashboard
        </button>

        <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2">
          <UploadCloud className="text-blue-600" /> Submeter Nova APO
        </h2>

        {status.message && (
          <div className={`p-4 mb-6 rounded-xl flex items-center gap-2 ${
            status.type === 'success' ? 'bg-green-50 text-green-700 border border-green-100' : 'bg-red-50 text-red-700 border border-red-100'
          }`}>
            {status.type === 'success' ? <CheckCircle size={20} /> : <AlertCircle size={20} />}
            {status.message}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Título da Atividade</label>
            <input 
              type="text" 
              required
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="Ex: Participação em Congresso de IA"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Descrição Detalhada</label>
            <textarea 
              required
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              rows="4"
              className="w-full px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
              placeholder="Descreva a atividade realizada e a sua relevância..."
            ></textarea>
          </div>

          <div className="border-2 border-dashed border-gray-200 rounded-xl p-8 text-center hover:bg-gray-50 transition-colors cursor-pointer group">
            <UploadCloud className="mx-auto h-10 w-10 text-gray-400 group-hover:text-blue-500 transition-colors" />
            <p className="mt-2 text-sm text-gray-500">Clique para anexar o certificado (PDF)</p>
          </div>

          <button 
            type="submit" 
            disabled={loading}
            className={`w-full py-3 px-4 bg-blue-600 text-white font-semibold rounded-xl hover:bg-blue-700 transition-all shadow-sm hover:shadow-md ${loading ? 'opacity-70 cursor-not-allowed' : ''}`}
          >
            {loading ? 'A Enviar...' : 'Enviar APO'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default SubmissaoApo;