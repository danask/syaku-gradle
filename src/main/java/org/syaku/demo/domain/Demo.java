package org.syaku.demo.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 4. 8.
 */

@Entity
@Table(name = "DEMO")
public class Demo implements Serializable {
	private static final long serialVersionUID = 2127649271884927110L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEMO_IDX")
	private long demo_idx;

	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private String value;

	public Demo() {
	}

	public Demo(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public long getDemo_idx() {
		return demo_idx;
	}

	public void setDemo_idx(long demo_idx) {
		this.demo_idx = demo_idx;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
