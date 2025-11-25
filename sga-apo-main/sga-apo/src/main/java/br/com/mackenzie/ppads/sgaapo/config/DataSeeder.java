package br.com.mackenzie.ppads.sgaapo.config;

import br.com.mackenzie.ppads.sgaapo.entity.Aluno;
import br.com.mackenzie.ppads.sgaapo.entity.Professor;
import br.com.mackenzie.ppads.sgaapo.entity.Secretaria;
import br.com.mackenzie.ppads.sgaapo.enus.Role; // Importe o Role
import br.com.mackenzie.ppads.sgaapo.repository.AlunoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.ProfessorRepository;
import br.com.mackenzie.ppads.sgaapo.repository.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder; // Importe o PasswordEncoder

/**
 * ATUALIZAÇÃO: Esta classe agora também injeta o PasswordEncoder
 * para salvar as senhas de teste com criptografia e definir os perfis (Roles).
 */
@Configuration
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private SecretariaRepository secretariaRepository;

    // --- MUDANÇA AQUI ---
    // Injeta o PasswordEncoder que vamos criar no SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("--- [DataSeeder] Inserindo dados de teste com criptografia ---");

        // Criando um Aluno de teste
        Aluno aluno1 = new Aluno();
        aluno1.setNome("Aluno Teste");
        aluno1.setEmail("aluno@teste.com");
        aluno1.setLogin("aluno");
        // --- MUDANÇA AQUI ---
        aluno1.setPassword(passwordEncoder.encode("senha123")); // Senha criptografada
        aluno1.setRole(Role.ROLE_ALUNO); // Define o perfil
        aluno1.setMatricula("123456");
        aluno1.setTotalPontosAcumulados(0);
        alunoRepository.save(aluno1);

        // Criando um Professor (Orientador/Avaliador) de teste
        Professor prof1 = new Professor();
        prof1.setNome("Professor Orientador Teste");
        prof1.setEmail("prof@teste.com");
        prof1.setLogin("professor");
        // --- MUDANÇA AQUI ---
        prof1.setPassword(passwordEncoder.encode("senha123")); // Senha criptografada
        prof1.setRole(Role.ROLE_PROFESSOR); // Define o perfil
        prof1.setDepartamento("FCI");
        professorRepository.save(prof1);

        // Criando uma Secretaria de teste
        Secretaria sec1 = new Secretaria();
        sec1.setNome("Secretaria Teste");
        sec1.setEmail("sec@teste.com");
        sec1.setLogin("secretaria");
        // --- MUDANÇA AQUI ---
        sec1.setPassword(passwordEncoder.encode("senha123")); // Senha criptografada
        sec1.setRole(Role.ROLE_SECRETARIA); // Define o perfil
        sec1.setSetor("PPGCA");
        secretariaRepository.save(sec1);
        
        System.out.println("--- [DataSeeder] Dados de teste inseridos ---");
    }
}