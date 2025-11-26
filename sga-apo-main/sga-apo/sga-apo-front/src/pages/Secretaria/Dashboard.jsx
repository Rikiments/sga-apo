import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { LogOut, CheckCircle, FileText, Clock, User } from 'lucide-react';

const DashboardSecretaria = () => {
    const navigate = useNavigate();
    const [apos, setApos] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPendenciasSecretaria();
    }, []);

    const fetchPendenciasSecretaria = async () => {
        try {
            // Busca tudo o que já passou pela Coordenação e está à espera da Secretaria
            const response = await api.get('/apos/status/AGUARDANDO_SECRETARIA');
            setApos(response.data);
        } catch (error) {
            console.error("Erro ao carregar tarefas", error);
        } finally {
            setLoading(false);
        }
    };

    const handleFinalizar = async (apoId) => {
        if (!window.confirm("Tem a certeza que deseja arquivar e finalizar este processo?")) return;

        try {
            await api.post(`/apos/${apoId}/finalizar`);
            alert("Processo finalizado com sucesso!");
            fetchPendenciasSecretaria();
        } catch (error) {
            console.error("Erro ao finalizar", error);
            alert("Erro ao finalizar.");
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-5xl mx-auto">
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-800">Painel da Secretaria</h1>
                        <p className="text-gray-500">Processos aprovados a aguardar finalização</p>
                    </div>
                    <button onClick={handleLogout} className="text-red-600 hover:bg-red-50 px-4 py-2 rounded-lg transition-colors flex items-center gap-2">
                        <LogOut size={18} /> Sair
                    </button>
                </div>

                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    {loading ? (
                        <p className="p-8 text-center text-gray-500">A carregar...</p>
                    ) : apos.length === 0 ? (
                        <div className="p-12 text-center">
                            <CheckCircle className="mx-auto text-green-500 w-12 h-12 mb-4" />
                            <h3 className="text-lg font-medium text-gray-900">Tudo Limpo!</h3>
                            <p className="text-gray-500">Não há processos aguardando finalização.</p>
                        </div>
                    ) : (
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="p-4 font-semibold text-gray-600">Título</th>
                                    <th className="p-4 font-semibold text-gray-600">Aluno</th>
                                    <th className="p-4 font-semibold text-gray-600">Data</th>
                                    <th className="p-4 font-semibold text-gray-600 text-right">Ação</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {apos.map((apo) => (
                                    <tr key={apo.id} className="hover:bg-gray-50 transition-colors">
                                        <td className="p-4">
                                            <div className="font-medium text-gray-900">{apo.titulo}</div>
                                            <div className="text-sm text-gray-500">{apo.descricao}</div>
                                        </td>
                                        <td className="p-4 text-gray-600">
                                            <div className="flex items-center gap-2">
                                                <User size={16} /> ID: {apo.id}
                                            </div>
                                        </td>
                                        <td className="p-4 text-gray-500 text-sm">
                                            <div className="flex items-center gap-2">
                                                <Clock size={16} /> {apo.dataSubmissao}
                                            </div>
                                        </td>
                                        <td className="p-4 text-right">
                                            <button 
                                                onClick={() => handleFinalizar(apo.id)}
                                                className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 font-medium text-sm transition-colors shadow-sm hover:shadow"
                                            >
                                                Finalizar & Arquivar
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </div>
    );
};

export default DashboardSecretaria;