package data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pacientes", schema = "cadastros")
public class Paciente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "data_nascimento")
	private Date nascimento;

	@Column(name = "data_obito")
	private Date obito;

	@Column(name = "data_entrada")
	private Date entrada;

	@Column(name = "nome_mae")
	private String mae;

	@Column(name = "nome_pai")
	private String pai;
	
	@Column(name = "rg_mae")
	private String rgMae;
	
	@Column(name = "rg_pai")
	private String rgPai;
	
	@Column(name = "cpf_mae")
	private String cpfMae;
	
	@Column(name = "cpf_pai")
	private String cpfPai;

	@Column(name = "rg")
	private String rg;
	
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "cep")
	private String cep;
	
	@Column(name = "telefone")
	private String telefone;
	
	@Column(name = "telefone_aux")
	private String telefoneAuxiliar;
	
	@Column(name = "local_nascimento")
	private String localNascimento;
	
	@Column(name = "rua_endereco")
	private String rua;
	
	private Integer logradouro;
	
	@Column(name = "diagnostico")
	private String diagnostico;
	
	@Column(name = "acompanhante")
	private String acompanhante;
	
	@Column(name = "telefone_acompanhante")
	private String telefoneAcompanhante;
	
	@Column(name = "cidade")
	private String cidade;
	
	@Column(name = "localidade")
	private Integer localidade;
	
	@Column (name = "localidade_nascimento")
	private Integer localidadeNascimento;
	
	@Column (name = "sexo")
	private String sexo;
	
	@Column (name = "cel_acompanhante")
	private String celularAcompanhante;
	
	@Column (name = "tel_comercial_acomp")
	private String telComercialAcompanhante;
	
	@Column (name = "observacao")
	private String observacao;
	
	@Column (name = "id_unidencaminhamento")
	private Integer localEncaminhado;
	
	@Column (name = "data_ultimoenc")
	private Date dataUltimoEncaminhamento;
	
	@Column (name = "data_ultimoretornoenc")
	private Date dataRetornoEncaminhamento;
	
	//@Column (name = "id_molestia")
	//private Integer molestia;
	
	@Column (name = "matricula")
	private Integer matricula;
	
	@Column (name = "uf")
	private Integer uf;
	
	@Transient
	private String estado;
	
	@Transient
	private String estadoNascimento;
	
	@Column (name = "id_ufnascimento")
	private Integer ufNascimento;
	
	@Transient
	private Boolean localNascimentoOk;
	
	@Transient
	private List<Molestia> molestias = new ArrayList<Molestia>();
	
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getLocalNascimento() {
		return localNascimento;
	}

	public void setLocalNascimento(String localNascimento) {
		this.localNascimento = localNascimento;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@Column(name = "complemento_endereco")
	private String complemento;

	@Transient
	private Boolean selecionado;

	//@Transient
	//private Acompanhante acompanhante = new Acompanhante();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public Date getObito() {
		return obito;
	}

	public void setObito(Date obito) {
		this.obito = obito;
	}

	public Date getEntrada() {
		return entrada;
	}

	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	public String getMae() {
		return mae;
	}

	public void setMae(String mae) {
		this.mae = mae;
	}

	public String getPai() {
		return pai;
	}

	public void setPai(String pai) {
		this.pai = pai;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	//public void setAcompanhante(Acompanhante acompanhante) {
	//	this.acompanhante = acompanhante;
	//}

	//public Acompanhante getAcompanhante() {
	//	return acompanhante;
	//}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setAcompanhante(String acompanhante) {
		this.acompanhante = acompanhante;
	}

	public String getAcompanhante() {
		return acompanhante;
	}

	public void setTelefoneAcompanhante(String telefoneAcompanhante) {
		this.telefoneAcompanhante = telefoneAcompanhante;
	}

	public String getTelefoneAcompanhante() {
		return telefoneAcompanhante;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCep() {
		return cep;
	}
	
	public String getCepSemFormatacao(){
		
		String saida = "";
		
		if(cep==null) return null;
		
		for(int i=0;i<cep.length();i++){
			if(Character.isDigit(cep.charAt(i))){
				saida = saida+cep.charAt(i);
			}
		}
		
		return saida;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCidade() {
		return cidade;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSexo() {
		return sexo;
	}

	public void setCelularAcompanhante(String celularAcompanhante) {
		this.celularAcompanhante = celularAcompanhante;
	}

	public String getCelularAcompanhante() {
		return celularAcompanhante;
	}

	public void setTelComercialAcompanhante(String telComercialAcompanhante) {
		this.telComercialAcompanhante = telComercialAcompanhante;
	}

	public String getTelComercialAcompanhante() {
		return telComercialAcompanhante;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setLocalEncaminhado(Integer localEncaminhado) {
		this.localEncaminhado = localEncaminhado;
	}

	public Integer getLocalEncaminhado() {
		return localEncaminhado;
	}

	public void setDataUltimoEncaminhamento(Date dataUltimoEncaminhamento) {
		this.dataUltimoEncaminhamento = dataUltimoEncaminhamento;
	}

	public Date getDataUltimoEncaminhamento() {
		return dataUltimoEncaminhamento;
	}
/*
	public void setMolestia(Integer molestia) {
		this.molestia = molestia;
	}

	public Integer getMolestia() {
		return molestia;
	}
*/
	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}

	public Integer getMatricula() {
		return matricula;
	}

	public void setUf(Integer uf) {
		this.uf = uf;
	}

	public Integer getUf() {
		return uf;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setLogradouro(Integer logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getLogradouro() {
		return logradouro;
	}

	public void setLocalidade(Integer localidade) {
		this.localidade = localidade;
	}

	public Integer getLocalidade() {
		return localidade;
	}

	public void setTelefoneAuxiliar(String telefoneAuxiliar) {
		this.telefoneAuxiliar = telefoneAuxiliar;
	}

	public String getTelefoneAuxiliar() {
		return telefoneAuxiliar;
	}

	public void setRgMae(String rgMae) {
		this.rgMae = rgMae;
	}

	public String getRgMae() {
		return rgMae;
	}

	public void setRgPai(String rgPai) {
		this.rgPai = rgPai;
	}

	public String getRgPai() {
		return rgPai;
	}

	public void setCpfMae(String cpfMae) {
		this.cpfMae = cpfMae;
	}

	public String getCpfMae() {
		return cpfMae;
	}

	public void setCpfPai(String cpfPai) {
		this.cpfPai = cpfPai;
	}

	public String getCpfPai() {
		return cpfPai;
	}

	public void setMolestias(List<Molestia> molestias) {
		this.molestias = molestias;
	}

	public List<Molestia> getMolestias() {
		return molestias;
	}

	public void setDataRetornoEncaminhamento(Date dataRetornoEncaminhamento) {
		this.dataRetornoEncaminhamento = dataRetornoEncaminhamento;
	}

	public Date getDataRetornoEncaminhamento() {
		return dataRetornoEncaminhamento;
	}

	public void setLocalNascimentoOk(Boolean localNascimentoOk) {
		this.localNascimentoOk = localNascimentoOk;
	}

	public Boolean getLocalNascimentoOk() {
		return localNascimentoOk;
	}

	public void setUfNascimento(Integer ufNascimento) {
		this.ufNascimento = ufNascimento;
	}

	public Integer getUfNascimento() {
		return ufNascimento;
	}

	public void setEstadoNascimento(String estadoNascimento) {
		this.estadoNascimento = estadoNascimento;
	}

	public String getEstadoNascimento() {
		return estadoNascimento;
	}

	public void setLocalidadeNascimento(Integer localidadeNascimento) {
		this.localidadeNascimento = localidadeNascimento;
	}

	public Integer getLocalidadeNascimento() {
		return localidadeNascimento;
	}

}
