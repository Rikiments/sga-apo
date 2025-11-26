import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { Plus, LogOut, FileText, Clock } from 'lucide-react';

const Dashboard = () => {
    const navigate = useNavigate();
    const [apos, setApos] = useState([]);
    const [loading, setLoading] = useState(true);

    // OBS: Num app real, pegaríamos o ID do token JWT decodificado. 
    // Como o DataSeeder do Java criou o aluno com ID 1, usamos 1 fixo por enquanto para testar.
    const ALUNO_ID = 1;

    useEffect(() => {
        fetchApos();
    }, []);

    const fetchApos = async () => {
        try {
            // Chama o endpoint GET que criamos no Java: /api/v1/apos/aluno/{id}
            const response = await api.get(`/apos/aluno/${ALUNO_ID}`);
            setApos(response.data);
        } catch (error) {
            console.error("Erro ao carregar APOs", error);
        } finally {
            setLoading(false);
        }
    };

    // Função para deixar o status bonito e colorido
    const getStatusColor = (status) => {
        if (!status) return 'text-gray-600 bg-gray-50 border-gray-200';
        if (status.includes('APROVADO') || status === 'FINALIZADO') return 'text-green-600 bg-green-50 border-green-200';
        if (status.includes('DEVOLVIDO')) return 'text-red-600 bg-red-50 border-red-200';
        return 'text-yellow-600 bg-yellow-50 border-yellow-200';
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userLogin');
        navigate('/login');
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-5xl mx-auto">
                
                {/* Cabeçalho */}
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-800">Painel do Aluno</h1>
                        <p className="text-gray-500">Gerencie as suas atividades complementares</p>
                    </div>
                    <button 
                        onClick={handleLogout} 
                        className="text-red-600 hover:bg-red-50 px-4 py-2 rounded-lg transition-colors flex items-center gap-2 font-medium"
                    >
                        <LogOut size={18} /> Sair
                    </button>
                </div>

                {/* Cards de Resumo */}
                <div className="grid gap-6 md:grid-cols-3 mb-8">
                    {/* Botão de Nova Submissão */}
                    <div 
                        onClick={() => navigate('/aluno/submeter')}
                        className="bg-blue-600 p-6 rounded-xl shadow-lg shadow-blue-200 text-white cursor-pointer hover:bg-blue-700 transition-all transform hover:-translate-y-1 flex flex-col justify-between group"
                    >
                        <div className="flex justify-between items-start">
                            <div>
                                <h3 className="font-bold text-lg">Nova Submissão</h3>
                                <p className="text-blue-100 text-sm mt-1">Enviar novo documento</p>
                            </div>
                            <div className="bg-white/20 p-2 rounded-lg group-hover:bg-white/30 transition-colors">
                                <Plus className="w-6 h-6" />
                            </div>
                        </div>
                    </div>
                    
                    {/* Card Total */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                        <div className="flex items-center gap-3 mb-2">
                            <div className="p-2 bg-gray-100 rounded-lg text-gray-600">
                                <FileText size={20} />
                            </div>
                            <h3 className="font-bold text-gray-700">Total Enviado</h3>
                        </div>
                        <p className="text-3xl font-bold text-gray-800">{apos.length}</p>
                    </div>

                    {/* Card Pontos */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                        <div className="flex items-center gap-3 mb-2">
                            <div className="p-2 bg-blue-50 rounded-lg text-blue-600">
                                <Clock size={20} />
                            </div>
                            <h3 className="font-bold text-gray-700">Meus Pontos</h3>
                        </div>
                        <p className="text-3xl font-bold text-blue-600">0 <span className="text-sm text-gray-400 font-normal">/ 12</span></p>
                    </div>
                </div>

                {/* Lista de APOs */}
                <h2 className="text-xl font-bold text-gray-800 mb-4">Minhas Atividades</h2>
                
                {loading ? (
                    <div className="text-center py-10">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
                        <p className="text-gray-500 mt-2">A carregar atividades...</p>
                    </div>
                ) : apos.length === 0 ? (
                    <div className="text-center py-12 bg-white rounded-xl border border-dashed border-gray-300">
                        <FileText className="mx-auto text-gray-300 w-12 h-12 mb-3" />
                        <p className="text-gray-500 font-medium">Nenhuma atividade encontrada.</p>
                        <p className="text-gray-400 text-sm">Clique no cartão azul para começar.</p>
                    </div>
                ) : (
                    <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                        {apos.map((apo) => (
                            <div key={apo.id} className="p-6 border-b border-gray-100 last:border-0 hover:bg-gray-50 transition-colors">
                                <div className="flex flex-col md:flex-row justify-between md:items-center gap-4">
                                    <div>
                                        <h3 className="font-bold text-lg text-gray-800">{apo.titulo}</h3>
                                        <p className="text-gray-600 text-sm mt-1">{apo.descricao}</p>
                                        <div className="flex items-center gap-4 mt-3 text-xs text-gray-500">
                                            <span className="flex items-center gap-1 bg-gray-100 px-2 py-1 rounded">
                                                <Clock size={12}/> {apo.dataSubmissao}
                                            </span>
                                            <span>Orientador: <span className="font-medium text-gray-700">{apo.nomeOrientador}</span></span>
                                        </div>
                                    </div>
                                    <span className={`px-3 py-1 rounded-full text-xs font-bold border ${getStatusColor(apo.status)}`}>
                                        {apo.status ? apo.status.replace(/_/g, ' ') : 'PENDENTE'}
                                    </span>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Dashboard;