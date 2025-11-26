import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import SubmissaoApo from './pages/Aluno/Submissao';
import Dashboard from './pages/Aluno/Dashboard';
import DashboardProfessor from './pages/Professor/Dashboard'; // Import Novo
import DashboardSecretaria from './pages/Secretaria/Dashboard';  

function App() {
  return (
    <Router>
      <Routes>
        {/* Rota de Login */}
        <Route path="/login" element={<Login />} />
        
        {/* Rotas do Aluno */}
        <Route path="/aluno/dashboard" element={<Dashboard />} />
        <Route path="/aluno/submeter" element={<SubmissaoApo />} />
        
        {/* Rota do Professor */}
        <Route path="/professor/dashboard" element={<DashboardProfessor />} />

        <Route path="/secretaria/dashboard" element={<DashboardSecretaria />} />
        
        {/* Redireciona qualquer rota desconhecida para o login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

export default App;