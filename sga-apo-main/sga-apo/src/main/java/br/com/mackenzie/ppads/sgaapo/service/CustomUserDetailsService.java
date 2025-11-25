package br.com.mackenzie.ppads.sgaapo.service;

import br.com.mackenzie.ppads.sgaapo.entity.Usuario;
import br.com.mackenzie.ppads.sgaapo.repository.AlunoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.ProfessorRepository;
import br.com.mackenzie.ppads.sgaapo.repository.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Este serviço é o "cérebro" do login.
 * O Spring Security vai chamar o método 'loadUserByUsername'
 * toda vez que alguém tentar se autenticar.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private SecretariaRepository secretariaRepository;

    /**
     * Procura um usuário pelo login em todos os repositórios de usuários.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // 1. Tenta encontrar como Aluno
        Optional<Usuario> aluno = alunoRepository.findByLogin(login).map(Usuario.class::cast);
        if (aluno.isPresent()) {
            return aluno.get();
        }

        // 2. Tenta encontrar como Professor
        Optional<Usuario> professor = professorRepository.findByLogin(login).map(Usuario.class::cast);
        if (professor.isPresent()) {
            return professor.get();
        }

        // 3. Tenta encontrar como Secretaria
        Optional<Usuario> secretaria = secretariaRepository.findByLogin(login).map(Usuario.class::cast);
        if (secretaria.isPresent()) {
            return secretaria.get();
        }

        // 4. Se não encontrar em nenhum, lança o erro
        throw new UsernameNotFoundException("Usuário não encontrado com o login: " + login);
    }
}