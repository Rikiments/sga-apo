import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { LogOut, CheckCircle, XCircle, Clock, User, Shield } from 'lucide-react';

const DashboardProfessor = () => {
    const navigate = useNavigate();
    const [apos, setApos] = useState([]);
    const [loading, setLoading] = useState(true);

    // Usamos ID 1 para coincidir com o DataSeeder
    const PROFESSOR_ID = 1; 

    useEffect(() => {
        fetchApos();
    }, []);

    const fetchApos = async () => {
        try {
            // Vamos buscar TODAS as APOs deste orientador, não só as pendentes,
            // para podermos ver o progresso delas.
            // Nota: Precisas de garantir que o teu backend tem um método para buscar tudo
            // ou usar o filtro de pendentes se ainda não alteraste o backend.
            // Se este endpoint der erro 404, usa o /pendentes que já tens.
            const response = await api.get(`/apos/orientador/${PROFESSOR_ID}/pendentes`);
            setApos(response.data);
        } catch (error) {
            console.error("Erro ao carregar APOs", error);
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    // Função genérica para avaliar em qualquer etapa
    const handleAvaliar = async (apoId, aprovado, etapa) => {
        const decisao = aprovado ? 'APROVADO' : 'DEVOLVIDO';
        const texto = `${decisao} pela etapa ${etapa}.`;
        
        if (!window.confirm(`Confirmar ${decisao} como ${etapa}?`)) return;

        try {
            await api.post(`/apos/${apoId}/avaliar`, {
                professorAvaliadorId: PROFESSOR_ID,
                parecer: texto,
                resultado: decisao,
                etapa: etapa
            });
            
            alert(`Sucesso! APO movida para a próxima fase.`);
            fetchApos(); // Recarrega a lista
        } catch (error) {
            console.error("Erro ao avaliar", error);
            alert('Erro ao processar avaliação.');
        }
    };

    const getStatusColor = (status) => {
        if (status.includes('ORIENTADOR')) return 'bg-blue-100 text-blue-700';
        if (status.includes('COMISSAO')) return 'bg-purple-100 text-purple-700';
        if (status.includes('COORDENACAO')) return 'bg-orange-100 text-orange-700';
        if (status.includes('SECRETARIA')) return 'bg-yellow-100 text-yellow-700';
        if (status.includes('FINALIZADO')) return 'bg-green-100 text-green-700';
        return 'bg-gray-100 text-gray-700';
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-5xl mx-auto">
                
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-800">Painel de Avaliação (Debug)</h1>
                        <p className="text-gray-500">Simule o fluxo completo de aprovação aqui.</p>
                    </div>
                    <button onClick={handleLogout} className="text-red-600 hover:bg-red-50 px-4 py-2 rounded-lg transition-colors flex items-center gap-2">
                        <LogOut size={18} /> Sair
                    </button>
                </div>

                <div className="grid gap-4">
                    {apos.map((apo) => (
                        <div key={apo.id} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                            <div className="flex flex-col md:flex-row justify-between gap-4 mb-4">
                                <div>
                                    <div className="flex items-center gap-2 mb-1">
                                        <span className={`px-2 py-1 text-xs font-bold rounded ${getStatusColor(apo.status)}`}>
                                            {apo.status}
                                        </span>
                                        <span className="text-gray-400 text-sm flex items-center gap-1"><Clock size={14}/> {apo.dataSubmissao}</span>
                                    </div>
                                    <h3 className="font-bold text-lg text-gray-800">{apo.titulo}</h3>
                                    <p className="text-gray-600 text-sm">{apo.descricao}</p>
                                </div>
                            </div>

                            {/* Botões de Ação para cada Etapa */}
                            <div className="grid grid-cols-1 md:grid-cols-3 gap-2 border-t pt-4 mt-2">
                                
                                {/* Botões do Orientador */}
                                {apo.status === 'EM_ANALISE_ORIENTADOR' && (
                                    <button 
                                        onClick={() => handleAvaliar(apo.id, true, 'ORIENTADOR')}
                                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 font-medium flex justify-center gap-2"
                                    >
                                        <User size={18} /> Aprovar (Orientador)
                                    </button>
                                )}

                                {/* Botões da Comissão (Simulação) */}
                                {apo.status === 'EM_ANALISE_COMISSAO' && (
                                    <button 
                                        onClick={() => handleAvaliar(apo.id, true, 'COMISSAO')}
                                        className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 font-medium flex justify-center gap-2"
                                    >
                                        <Shield size={18} /> Aprovar (Comissão)
                                    </button>
                                )}

                                {/* Botões da Coordenação (Simulação) */}
                                {apo.status === 'EM_ANALISE_COORDENACAO' && (
                                    <button 
                                        onClick={() => handleAvaliar(apo.id, true, 'COORDENACAO')}
                                        className="bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 font-medium flex justify-center gap-2"
                                    >
                                        <CheckCircle size={18} /> Aprovar (Coordenação)
                                    </button>
                                )}

                                {apo.status === 'AGUARDANDO_SECRETARIA' && (
                                    <div className="col-span-3 text-center text-yellow-600 font-medium p-2 bg-yellow-50 rounded-lg">
                                        Pronto para finalização pela Secretaria.
                                    </div>
                                )}
                            </div>
                        </div>
                    ))}

                    {apos.length === 0 && (
                        <p className="text-center text-gray-500 py-10">Nenhuma APO encontrada.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default DashboardProfessor;