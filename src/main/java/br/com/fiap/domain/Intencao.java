package br.com.fiap.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Intencao.
 */
@Entity
@Table(name = "intencao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Intencao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor_estimado", precision = 21, scale = 2)
    private BigDecimal valorEstimado;

    @Column(name = "data")
    private LocalDate data;

    @OneToOne
    @JoinColumn(unique = true)
    private Produto produto;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Intencao id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Intencao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorEstimado() {
        return this.valorEstimado;
    }

    public Intencao valorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
        return this;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Intencao data(LocalDate data) {
        this.data = data;
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public Intencao produto(Produto produto) {
        this.setProduto(produto);
        return this;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intencao)) {
            return false;
        }
        return id != null && id.equals(((Intencao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intencao{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", valorEstimado=" + getValorEstimado() +
            ", data='" + getData() + "'" +
            "}";
    }
}
